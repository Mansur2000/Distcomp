package topic

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"time"

	"lab1/internal/model"

	"github.com/jmoiron/sqlx"
	"github.com/lib/pq"
)

var (
	ErrTopicNotFound     = fmt.Errorf("topics not found")
	ErrInvalidTopicData  = fmt.Errorf("invalid topic data")
	ErrInvalidForeignKey = fmt.Errorf("invalid foreign key passed")
)

type Repo interface {
	Create(ctx context.Context, req model.Topic) (model.Topic, error)
	GetList(ctx context.Context) ([]model.Topic, error)
	Get(ctx context.Context, id int64) (model.Topic, error)
	Update(ctx context.Context, req model.Topic) (model.Topic, error)
	Delete(ctx context.Context, id int64) error
}

type repo struct {
	db *sqlx.DB
}

func New(db *sqlx.DB) Repo {
	return repo{
		db: db,
	}
}

func (r repo) Create(ctx context.Context, req model.Topic) (model.Topic, error) {
	query := `INSERT INTO topic (creator_id, title, content) 
	          VALUES ($1, $2, $3) RETURNING id, created`

	var id int64
	var created time.Time

	err := r.db.QueryRowContext(ctx, query, req.CreatorID, req.Title, req.Content).Scan(&id, &created)
	if err != nil {
		var pqErr *pq.Error
		if errors.As(err, &pqErr) && pqErr.Code == "23503" {
			return model.Topic{}, ErrTopicNotFound
		}

		if errors.As(err, &pqErr) && pqErr.Code == "23505" {
			return model.Topic{}, ErrInvalidTopicData
		}

		return model.Topic{}, fmt.Errorf("failed to create topic: %w", err)
	}

	req.ID = id
	req.Created = created

	return req, nil
}

func (r repo) GetList(ctx context.Context) ([]model.Topic, error) {
	var result []model.Topic

	query := `SELECT * FROM topic`

	err := r.db.SelectContext(ctx, &result, query)
	if err != nil {
		return nil, fmt.Errorf("failed to retrieve result: %w", err)
	}

	if len(result) == 0 {
		return []model.Topic{}, nil
	}

	return result, nil
}

func (r repo) Get(ctx context.Context, id int64) (model.Topic, error) {
	var result model.Topic

	query := `SELECT id, creator_id, title, content, created, modified FROM topic WHERE id = $1`

	err := r.db.GetContext(ctx, &result, query, id)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return result, ErrTopicNotFound
		}

		return result, fmt.Errorf("failed to retrieve topic by ID: %w", err)
	}

	return result, nil
}

func (r repo) Update(ctx context.Context, req model.Topic) (model.Topic, error) {
	var result model.Topic

	query := `UPDATE topic SET creator_id = $1, title = $2, content = $3, modified = $4 
	          WHERE id = $5 RETURNING id, creator_id, title, content, created, modified`

	err := r.db.QueryRowContext(ctx, query, req.CreatorID, req.Title, req.Content, req.Modified, req.ID).
		Scan(&result.ID, &result.CreatorID, &result.Title, &result.Content, &result.Created, &result.Modified)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return result, ErrTopicNotFound
		}

		return result, fmt.Errorf("failed to update topic: %w", err)
	}

	return result, nil
}

func (r repo) Delete(ctx context.Context, id int64) error {
	query := `DELETE FROM topic WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete topic: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to check rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return ErrTopicNotFound
	}

	return nil
}
