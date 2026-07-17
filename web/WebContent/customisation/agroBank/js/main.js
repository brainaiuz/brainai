const passwordInput = document.getElementById('password');

const toggleInputType = () =>
        passwordInput.type === 'password'
                ? (passwordInput.type = 'text')
                : (passwordInput.type = 'password');
