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


