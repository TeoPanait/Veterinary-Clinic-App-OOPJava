-- migrations/001_create_prescriptions.sql
-- Creates medications (if missing) and prescriptions table

CREATE TABLE IF NOT EXISTS medications (
  medication_id SERIAL PRIMARY KEY,
  med_name VARCHAR(255) NOT NULL,
  dosage VARCHAR(100),
  price NUMERIC(10,2) DEFAULT 0,
  stock INT DEFAULT 0 CHECK (stock >= 0),
  req_prescription BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS prescriptions (
  prescription_id SERIAL PRIMARY KEY,
  record_id INT NOT NULL REFERENCES medical_records(record_id) ON DELETE CASCADE,
  medication_id INT NOT NULL REFERENCES medications(medication_id) ON DELETE RESTRICT,
  quantity INT NOT NULL CHECK (quantity > 0),
  prescribed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_prescriptions_record ON prescriptions(record_id);
CREATE INDEX IF NOT EXISTS idx_prescriptions_med ON prescriptions(medication_id);

