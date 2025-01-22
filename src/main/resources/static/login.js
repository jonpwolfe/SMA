document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // Prevent the form from submitting the default way

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Prepare the login payload
    const loginData = {
        username: username,
        password: password
    };

    try {
        // Send a POST request to the server
        const response = await fetch('http://example.com/api/auth', { // Replace with your login endpoint
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginData)
        });

        // Handle the response
        if (response.ok) {
            const data = await response.json();
            // Save token or perform other actions
            console.log('Login successful:', data);
            // Redirect to a secure page or update the UI
            window.location.href = 'index.html'; // Redirect after login
        } else {
            const errorData = await response.json();
            document.getElementById('error-message').innerText = errorData.message || 'Login failed!';
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('error-message').innerText = 'An error occurred. Please try again.';
    }
});
