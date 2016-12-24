CREATE TABLE IF NOT EXISTS users (
    user_id UUID NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE users IS 'Keep recruiter and candidate data centralized';
COMMENT ON COLUMN users.user_id IS 'User id';
COMMENT ON COLUMN users.email IS 'User email';
COMMENT ON COLUMN users.name IS 'Name and surname';
COMMENT ON COLUMN users.created IS 'When the user registered';
COMMENT ON COLUMN users.updated IS 'When the user updated its information';