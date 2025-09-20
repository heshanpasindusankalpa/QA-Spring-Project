import requests

# Test the login endpoint first
response = requests.post(
    'http://localhost:8080/api/auth/login',
    json={'username': 'testuser', 'password': 'Password123!'},
    headers={'Content-Type': 'application/json'}
)

print(f"Login Response: {response.status_code}")
print(f"Response Time: {response.elapsed.total_seconds()} seconds")