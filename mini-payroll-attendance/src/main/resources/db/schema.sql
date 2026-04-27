-- =============================================================
-- Mini Payroll & Attendance System - Database Schema
-- PostgreSQL 14+
-- Run this script once to set up the database from scratch.
-- =============================================================

-- Create database (run this separately as a superuser if needed)
-- CREATE DATABASE payroll_db;
-- \c payroll_db

-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =============================================================
-- ENUMS
-- =============================================================

DO $$ BEGIN
    CREATE TYPE employee_role AS ENUM ('WFH', 'OFFICE', 'ONSITE');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE salary_type AS ENUM ('MONTHLY', 'DAILY');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE system_role AS ENUM ('ADMIN', 'EMPLOYEE');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE attendance_status AS ENUM ('PRESENT', 'ABSENT');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE leave_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

-- =============================================================
-- TABLE: employees
-- =============================================================

CREATE TABLE IF NOT EXISTS employees (
    id            UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    name          VARCHAR(255)  NOT NULL,
    role          VARCHAR(20)   NOT NULL CHECK (role IN ('WFH', 'OFFICE', 'ONSITE')),
    salary_type   VARCHAR(20)   NOT NULL CHECK (salary_type IN ('MONTHLY', 'DAILY')),
    salary_amount DECIMAL(10,2) NOT NULL CHECK (salary_amount > 0),
    system_role   VARCHAR(20)   NOT NULL CHECK (system_role IN ('ADMIN', 'EMPLOYEE')),
    username      VARCHAR(100)  NOT NULL UNIQUE,
    password      VARCHAR(255)  NOT NULL,
    created_at    TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- =============================================================
-- TABLE: attendance
-- =============================================================

CREATE TABLE IF NOT EXISTS attendance (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID        NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    date        DATE        NOT NULL,
    status      VARCHAR(10) NOT NULL CHECK (status IN ('PRESENT', 'ABSENT')),
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),

    -- One record per employee per day
    CONSTRAINT uq_attendance_employee_date UNIQUE (employee_id, date)
);

-- =============================================================
-- TABLE: leaves
-- =============================================================

CREATE TABLE IF NOT EXISTS leaves (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID        NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
    status      VARCHAR(10) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    reason      TEXT,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),

    -- end_date must not be before start_date
    CONSTRAINT chk_leave_dates CHECK (end_date >= start_date)
);

-- =============================================================
-- INDEXES for performance
-- =============================================================

-- Attendance lookups by employee
CREATE INDEX IF NOT EXISTS idx_attendance_employee_id ON attendance(employee_id);
-- Attendance lookups by date range (used in payroll calculation)
CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendance(date);
-- Attendance lookups by employee + date range + status (payroll query)
CREATE INDEX IF NOT EXISTS idx_attendance_employee_date_status ON attendance(employee_id, date, status);

-- Leave lookups by employee
CREATE INDEX IF NOT EXISTS idx_leaves_employee_id ON leaves(employee_id);
-- Leave lookups by status (admin review)
CREATE INDEX IF NOT EXISTS idx_leaves_status ON leaves(status);
-- Leave lookups by date range (payroll approved leave calculation)
CREATE INDEX IF NOT EXISTS idx_leaves_date_range ON leaves(start_date, end_date);

-- Employee lookup by username (login)
CREATE INDEX IF NOT EXISTS idx_employees_username ON employees(username);
