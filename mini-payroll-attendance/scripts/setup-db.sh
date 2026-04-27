#!/bin/bash
# =============================================================
# Mini Payroll & Attendance System - Database Setup Script
# Usage: bash backend/scripts/setup-db.sh
# =============================================================

set -e

# ---------- Configuration (edit these if needed) ----------
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-payroll_db}"
DB_USER="${DB_USER:-postgres}"
# ----------------------------------------------------------

SCHEMA_FILE="$(dirname "$0")/../src/main/resources/db/schema.sql"
SEED_FILE="$(dirname "$0")/../src/main/resources/db/seed.sql"

echo "============================================="
echo " Payroll DB Setup"
echo " Host : $DB_HOST:$DB_PORT"
echo " DB   : $DB_NAME"
echo " User : $DB_USER"
echo "============================================="

# Step 1: Create database if it doesn't exist
echo ""
echo "[1/3] Creating database '$DB_NAME' (if not exists)..."
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -tc \
  "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" \
  | grep -q 1 || \
  psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" \
    -c "CREATE DATABASE $DB_NAME;"
echo "      Done."

# Step 2: Run schema
echo ""
echo "[2/3] Running schema.sql..."
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$SCHEMA_FILE"
echo "      Done."

# Step 3: Run seed data
echo ""
echo "[3/3] Running seed.sql..."
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$SEED_FILE"
echo "      Done."

echo ""
echo "============================================="
echo " Setup complete!"
echo ""
echo " Login credentials:"
echo "   Admin    -> username: admin    / password: admin123"
echo "   Employee -> username: alice    / password: emp123"
echo "   Employee -> username: bob      / password: emp123"
echo "   Employee -> username: carol    / password: emp123"
echo ""
echo " Start the backend:"
echo "   cd backend"
echo "   DATABASE_URL=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \\"
echo "   DATABASE_USERNAME=$DB_USER \\"
echo "   DATABASE_PASSWORD=<your-password> \\"
echo "   JWT_SECRET=my-super-secret-jwt-key-that-is-at-least-32-characters-long \\"
echo "   mvn spring-boot:run"
echo "============================================="
