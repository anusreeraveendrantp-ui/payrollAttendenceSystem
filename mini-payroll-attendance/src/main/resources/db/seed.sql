-- =============================================================
-- Mini Payroll & Attendance System - Seed Data
-- Run AFTER schema.sql
-- =============================================================
-- Passwords are BCrypt hashed.
-- admin123  -> $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- emp123    -> $2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNwojr1Tz6.wyv0UXSS
-- =============================================================

-- Clear existing data (safe for dev/test resets)
TRUNCATE TABLE leaves     RESTART IDENTITY CASCADE;
TRUNCATE TABLE attendance RESTART IDENTITY CASCADE;
DELETE FROM employees;

-- =============================================================
-- EMPLOYEES
-- =============================================================

-- Admin user
INSERT INTO employees (id, name, role, salary_type, salary_amount, system_role, username, password)
VALUES (
    'a0000000-0000-0000-0000-000000000001',
    'Admin User',
    'OFFICE',
    'MONTHLY',
    50000.00,
    'ADMIN',
    'admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'  -- admin123
)
ON CONFLICT (username) DO NOTHING;

-- Employee 1 - Monthly salary, Office
INSERT INTO employees (id, name, role, salary_type, salary_amount, system_role, username, password)
VALUES (
    'b0000000-0000-0000-0000-000000000001',
    'Alice Johnson',
    'OFFICE',
    'MONTHLY',
    45000.00,
    'EMPLOYEE',
    'alice',
    '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNwojr1Tz6.wyv0UXSS'  -- emp123
)
ON CONFLICT (username) DO NOTHING;

-- Employee 2 - Daily salary, WFH
INSERT INTO employees (id, name, role, salary_type, salary_amount, system_role, username, password)
VALUES (
    'b0000000-0000-0000-0000-000000000002',
    'Bob Smith',
    'WFH',
    'DAILY',
    1500.00,
    'EMPLOYEE',
    'bob',
    '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNwojr1Tz6.wyv0UXSS'  -- emp123
)
ON CONFLICT (username) DO NOTHING;

-- Employee 3 - Monthly salary, Onsite
INSERT INTO employees (id, name, role, salary_type, salary_amount, system_role, username, password)
VALUES (
    'b0000000-0000-0000-0000-000000000003',
    'Carol White',
    'ONSITE',
    'MONTHLY',
    60000.00,
    'EMPLOYEE',
    'carol',
    '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNwojr1Tz6.wyv0UXSS'  -- emp123
)
ON CONFLICT (username) DO NOTHING;

-- =============================================================
-- ATTENDANCE - April 2026 (sample data for payroll testing)
-- =============================================================

-- Alice: 20 present days in April 2026
INSERT INTO attendance (employee_id, date, status) VALUES
('b0000000-0000-0000-0000-000000000001', '2026-04-01', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-02', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-03', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-04', 'ABSENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-05', 'ABSENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-06', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-07', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-08', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-09', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-10', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-11', 'ABSENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-12', 'ABSENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-13', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-14', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-15', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-16', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-17', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-18', 'ABSENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-19', 'ABSENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-20', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-21', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-22', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-23', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-24', 'PRESENT'),
('b0000000-0000-0000-0000-000000000001', '2026-04-25', 'PRESENT')
ON CONFLICT (employee_id, date) DO NOTHING;

-- Bob: 18 present days in April 2026
INSERT INTO attendance (employee_id, date, status) VALUES
('b0000000-0000-0000-0000-000000000002', '2026-04-01', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-02', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-03', 'ABSENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-04', 'ABSENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-05', 'ABSENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-06', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-07', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-08', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-09', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-10', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-13', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-14', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-15', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-16', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-17', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-20', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-21', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-22', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-23', 'ABSENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-24', 'PRESENT'),
('b0000000-0000-0000-0000-000000000002', '2026-04-25', 'PRESENT')
ON CONFLICT (employee_id, date) DO NOTHING;

-- Carol: 22 present days in April 2026
INSERT INTO attendance (employee_id, date, status) VALUES
('b0000000-0000-0000-0000-000000000003', '2026-04-01', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-02', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-03', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-06', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-07', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-08', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-09', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-10', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-13', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-14', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-15', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-16', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-17', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-20', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-21', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-22', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-23', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-24', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-25', 'PRESENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-04', 'ABSENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-05', 'ABSENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-11', 'ABSENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-12', 'ABSENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-18', 'ABSENT'),
('b0000000-0000-0000-0000-000000000003', '2026-04-19', 'ABSENT')
ON CONFLICT (employee_id, date) DO NOTHING;

-- =============================================================
-- LEAVES - Sample leave requests
-- =============================================================

-- Alice: approved leave Apr 4-5 (2 days) - already absent those days
INSERT INTO leaves (employee_id, start_date, end_date, status, reason)
VALUES (
    'b0000000-0000-0000-0000-000000000001',
    '2026-04-04',
    '2026-04-05',
    'APPROVED',
    'Personal appointment'
);

-- Bob: pending leave request
INSERT INTO leaves (employee_id, start_date, end_date, status, reason)
VALUES (
    'b0000000-0000-0000-0000-000000000002',
    '2026-04-26',
    '2026-04-28',
    'PENDING',
    'Family event'
);

-- Carol: rejected leave
INSERT INTO leaves (employee_id, start_date, end_date, status, reason)
VALUES (
    'b0000000-0000-0000-0000-000000000003',
    '2026-04-11',
    '2026-04-12',
    'REJECTED',
    'Vacation'
);

-- Carol: approved leave
INSERT INTO leaves (employee_id, start_date, end_date, status, reason)
VALUES (
    'b0000000-0000-0000-0000-000000000003',
    '2026-04-18',
    '2026-04-19',
    'APPROVED',
    'Medical leave'
);

-- =============================================================
-- VERIFY
-- =============================================================
SELECT 'employees' AS table_name, COUNT(*) AS row_count FROM employees
UNION ALL
SELECT 'attendance', COUNT(*) FROM attendance
UNION ALL
SELECT 'leaves',     COUNT(*) FROM leaves;
