-- Enable UUID generation if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. Users Table
CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    avatar TEXT,
    role TEXT DEFAULT 'USER',
    level TEXT,
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_login_at TIMESTAMP WITH TIME ZONE,
                            is_active BOOLEAN DEFAULT TRUE
                            );

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);

---

-- 3. User Credentials Table
-- Stores hashed passwords and other authentication-related details.
-- No foreign key constraint here, relies on application logic.
CREATE TABLE IF NOT EXISTS user_credentials (
    user_id UUID PRIMARY KEY,
    password_hash TEXT NOT NULL,
    last_password_change_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    failed_login_attempts INTEGER DEFAULT 0,
    locked_until TIMESTAMP WITH TIME ZONE -- Account locked until this time
);

---

-- 4. Quizzes Table
-- No foreign key constraint here, relies on application logic.
CREATE TABLE IF NOT EXISTS quizzes (
                                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    description TEXT,
    category TEXT ,
    difficulty TEXT,
    duration INTEGER, -- in minutes
    question_count INTEGER  DEFAULT 0,
    tags TEXT[], -- PostgreSQL array of strings
    is_public BOOLEAN ,
    created_at TIMESTAMP WITH TIME ZONE  DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE  DEFAULT NOW(),
    author_id UUID , -- This column still exists, but no DB-level foreign key
    rating NUMERIC(3, 2) DEFAULT 0.00, -- e.g., 4.50
    attempts INTEGER  DEFAULT 0,
    passing_score INTEGER, -- Percentage (e.g., 70 for 70%)
    navigation_mode TEXT ,
    has_timer BOOLEAN  DEFAULT FALSE,
    time_limit INTEGER, -- in seconds
    warning_time INTEGER, -- in seconds
    allow_question_picker BOOLEAN  DEFAULT FALSE,
    shuffle_questions BOOLEAN  DEFAULT FALSE,
    shuffle_answers BOOLEAN  DEFAULT FALSE,
    show_progress BOOLEAN  DEFAULT FALSE,
    allow_pause BOOLEAN  DEFAULT FALSE,
    max_attempts INTEGER -- NULL for unlimited attempts
    );

CREATE INDEX IF NOT EXISTS idx_quizzes_author_id ON quizzes (author_id);

---

-- 5. Questions Table
-- No foreign key constraint here, relies on application logic.
CREATE TABLE IF NOT EXISTS questions (
                                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quiz_id UUID NOT NULL, -- This column still exists, but no DB-level foreign key
    type TEXT,
    text TEXT,
    options JSONB,
    pronunciation_text TEXT,
    fill_in_blanks JSONB,
    true_false_answer BOOLEAN,
    audio_url TEXT,
    image_url TEXT,
    max_listening_time INTEGER, -- in seconds
    correct_answer TEXT[],
    explanation TEXT,
    points INTEGER,
    time_limit INTEGER, -- in seconds
    difficulty TEXT,
    category TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
    );

CREATE INDEX IF NOT EXISTS idx_questions_quiz_id ON questions (quiz_id);

---

-- 6. Quiz Attempts Table
-- No foreign key constraints here, relies on application logic.
CREATE TABLE IF NOT EXISTS quiz_attempts (
                                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quiz_id UUID NOT NULL, -- This column still exists, but no DB-level foreign key
    user_id UUID NOT NULL, -- This column still exists, but no DB-level foreign key
    score NUMERIC(5, 2),
    total_questions INTEGER,
    correct_answers INTEGER,
    time_spent INTEGER,
    completed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    answers JSONB,
    passed BOOLEAN
    );

CREATE INDEX IF NOT EXISTS idx_quiz_attempts_quiz_id ON quiz_attempts (quiz_id);
CREATE INDEX IF NOT EXISTS idx_quiz_attempts_user_id ON quiz_attempts (user_id);

---s

-- 7. User Saved Quizzes Table
-- No foreign key constraints here, relies on application logic.
CREATE TABLE IF NOT EXISTS user_saved_quizzes (
                                                  user_id UUID NOT NULL, -- This column still exists, but no DB-level foreign key
                                                  quiz_id UUID NOT NULL, -- This column still exists, but no DB-level foreign key
                                                  saved_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, quiz_id)
    );

CREATE INDEX IF NOT EXISTS idx_user_saved_quizzes_user_id ON user_saved_quizzes (user_id);
CREATE INDEX IF NOT EXISTS idx_user_saved_quizzes_quiz_id ON user_saved_quizzes (quiz_id);

---

-- 8. Leaderboard
-- No foreign key constraints here, relies on application logic.
CREATE TABLE IF NOT EXISTS leaderboard (
                                           quiz_id UUID NOT NULL, -- This column still exists, but no DB-level foreign key
                                           user_id UUID NOT NULL, -- This column still exists, but no DB-level foreign key
                                           score NUMERIC(5, 2),
    completion_time INTEGER,
    accuracy NUMERIC(3, 2),
    attempts INTEGER,
    last_attempt TIMESTAMP WITH TIME ZONE,
                               rank INTEGER,
                               PRIMARY KEY (quiz_id, user_id)
    );

CREATE INDEX IF NOT EXISTS idx_leaderboard_quiz_id ON leaderboard (quiz_id);
CREATE INDEX IF NOT EXISTS idx_leaderboard_user_id ON leaderboard (user_id);
CREATE INDEX IF NOT EXISTS idx_leaderboard_score_time ON leaderboard (quiz_id, score DESC, completion_time ASC);