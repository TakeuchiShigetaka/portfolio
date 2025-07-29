-- テーブル定義

-- users テーブル
-- kakeibo_db.users definition

CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(40) COLLATE utf8mb4_general_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- default_categories テーブル
-- kakeibo_db.default_categories definition

CREATE TABLE `default_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- user_categories テーブル
-- kakeibo_db.user_categories definition

CREATE TABLE `user_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_categories` (`user_id`,`name`),
  CONSTRAINT `user_categories_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- default_feelings テーブル
-- kakeibo_db.default_feelings definition

CREATE TABLE `default_feelings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `polarity` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- user_feelings テーブル
-- kakeibo_db.user_feelings definition

CREATE TABLE `user_feelings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `polarity` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_feelings` (`user_id`,`name`),
  CONSTRAINT `user_feelings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- expenses テーブル
-- kakeibo_db.expenses definition

CREATE TABLE `expenses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `amount` int NOT NULL,
  `category_id` bigint NOT NULL,
  `feeling_id` bigint NOT NULL,
  `date` date NOT NULL,
  `memo` varchar(255) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `category_is_default` tinyint(1) NOT NULL DEFAULT '0',
  `feeling_is_default` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `category_id` (`category_id`),
  KEY `feeling_id` (`feeling_id`),
  CONSTRAINT `expenses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- monthly_goals テーブル
-- kakeibo_db.monthly_goals definition

CREATE TABLE `monthly_goals` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `year` int NOT NULL,
  `month` int NOT NULL,
  `goal_amount` int NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`year`,`month`),
  CONSTRAINT `monthly_goals_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- monthly_category_goals テーブル
-- kakeibo_db.monthly_category_goals definition

CREATE TABLE `monthly_category_goals` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `year` int NOT NULL,
  `month` int NOT NULL,
  `category_id` bigint NOT NULL,
  `category_is_default` tinyint(1) NOT NULL DEFAULT '0',
  `goal_amount` decimal(10,2) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_year_month_category_combination` (`year`,`month`,`category_id`,`category_is_default`,`user_id`),
  KEY `fk_mcg_user` (`user_id`),
  CONSTRAINT `fk_mcg_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 初期データ

-- default_categories  テーブル 初期データ
INSERT INTO kakeibo_db.default_categories (name,created_at,updated_at) VALUES
	 ('食費','2025-07-04 23:16:12','2025-07-04 23:16:12'),
	 ('日用品','2025-07-04 23:16:12','2025-07-04 23:16:12'),
	 ('交際費','2025-07-04 23:16:12','2025-07-04 23:16:12');
	 
-- default_feelings テーブル 初期データ
INSERT INTO kakeibo_db.default_feelings (name,polarity,created_at,updated_at) VALUES
	 ('満足','positive','2025-07-04 23:19:24','2025-07-04 23:19:24'),
	 ('後悔','negative','2025-07-04 23:19:24','2025-07-04 23:19:24'),
	 ('期待','positive','2025-07-04 23:19:24','2025-07-04 23:19:24');


