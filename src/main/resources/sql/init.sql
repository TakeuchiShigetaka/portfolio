-- テーブル定義

--DROP TABLE IF EXISTS users CASCADE;
--DROP TABLE IF EXISTS default_categories CASCADE;
--DROP TABLE IF EXISTS user_categories CASCADE;
--DROP TABLE IF EXISTS default_feelings CASCADE;
--DROP TABLE IF EXISTS user_feelings CASCADE;
--DROP TABLE IF EXISTS expenses CASCADE;
--DROP TABLE IF EXISTS monthly_goals CASCADE;
--DROP TABLE IF EXISTS monthly_category_goals CASCADE;

-- users テーブル
-- kakeibo_db.users definition


CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  email VARCHAR(40) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

-- default_categories テーブル
-- kakeibo_db.default_categories definition

CREATE TABLE default_categories (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

-- user_categories テーブル
-- kakeibo_db.user_categories definition

CREATE TABLE user_categories (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  name VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  CONSTRAINT uq_user_categories UNIQUE (user_id, name),
  CONSTRAINT fk_user_categories_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- default_feelings テーブル
-- kakeibo_db.default_feelings definition

CREATE TABLE default_feelings (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  polarity VARCHAR(10) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

-- user_feelings テーブル
-- kakeibo_db.user_feelings definition

CREATE TABLE user_feelings (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  name VARCHAR(50) NOT NULL,
  polarity VARCHAR(10) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  CONSTRAINT uq_user_feelings UNIQUE (user_id, name),
  CONSTRAINT fk_user_feelings_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- expenses テーブル
-- kakeibo_db.expenses definition

CREATE TABLE expenses (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  amount INTEGER NOT NULL,
  category_id BIGINT NOT NULL,
  feeling_id BIGINT NOT NULL,
  date DATE NOT NULL,
  memo VARCHAR(255) NOT NULL DEFAULT '',
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  category_is_default BOOLEAN NOT NULL DEFAULT FALSE,
  feeling_is_default BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
-- monthly_goals テーブル
-- kakeibo_db.monthly_goals definition

CREATE TABLE monthly_goals (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  year INTEGER NOT NULL,
  month INTEGER NOT NULL,
  goal_amount INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  CONSTRAINT uq_user_monthly UNIQUE (user_id, year, month),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
-- monthly_category_goals テーブル
-- kakeibo_db.monthly_category_goals definition

CREATE TABLE monthly_category_goals (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT,
  year INTEGER NOT NULL,
  month INTEGER NOT NULL,
  category_id BIGINT NOT NULL,
  category_is_default BOOLEAN NOT NULL DEFAULT FALSE,
  goal_amount DECIMAL(10, 2) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT uq_monthly_cat_goal UNIQUE (year, month, category_id, category_is_default, user_id),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- 初期データ

-- default_categories 初期データ
INSERT INTO default_categories (name, created_at, updated_at) VALUES
  ('食費', '2025-07-04 23:16:12', '2025-07-04 23:16:12'),
  ('日用品', '2025-07-04 23:16:12', '2025-07-04 23:16:12'),
  ('交際費', '2025-07-04 23:16:12', '2025-07-04 23:16:12');

-- default_feelings 初期データ
INSERT INTO default_feelings (name, polarity, created_at, updated_at) VALUES
  ('満足', 'positive', '2025-07-04 23:19:24', '2025-07-04 23:19:24'),
  ('後悔', 'negative', '2025-07-04 23:19:24', '2025-07-04 23:19:24'),
  ('期待', 'positive', '2025-07-04 23:19:24', '2025-07-04 23:19:24');
