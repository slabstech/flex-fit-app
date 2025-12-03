# Register
curl -X POST https://aaa/register/ -H "Content-Type: application/json" -d '{"username":"testuser","email":"testuser@flexfit.com","password":"123456"}' && echo

# Login
curl -X POST https://aaa/login -H "Content-Type: application/x-www-form-urlencoded" -d "username=testuser@flexfit.com&password=123456" && echo