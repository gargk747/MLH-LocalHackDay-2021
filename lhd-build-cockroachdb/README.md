# lhd-build-cockroachdb
This simple application follows the hello world tutorial available at: https://www.cockroachlabs.com/docs/cockroachcloud/build-a-python-app-with-cockroachdb-django.html

## Quick Start
0. Set up virtual environment
   ```
   python3 -m venv env
   source env/bin/activate
   ```
   
1. Install dependencies (django etc)
   ```
   python -m pip install django
   pip install psycopg2-binary
   python -m pip install django-cockroachdb
   ```
   
   > Note: If you have any errors when installing `psycopg2-binary`, upgrade pip to 20.3.3 or newer:
   ```
   python -m pip install --upgrade pip
   ```
   
2. Start application
   ```
   python manage.py runserver 0.0.0.0:8000
   ```
