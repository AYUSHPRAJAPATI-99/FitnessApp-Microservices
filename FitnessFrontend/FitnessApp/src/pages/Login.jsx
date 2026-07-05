import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('login');
  const navigate = useNavigate();
  const prefixUrl = 'http://localhost:8080';


  function loginOauth() {
    window.location.href =
        "http://localhost:8080/oauth2/authorization/google";
  }
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const res = await fetch(`/${prefixUrl}/api/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),     // by defuatl json email:email
      });
      const data = await res.json();

      if (res.ok) {
        navigate('/activities');
      } else {
        setError(data.error || 'Login failed');
      }
    } catch (err) {
      setError('Network error. Please try again.');
    } finally {
      setLoading(false);
    }
  }
  return (
     <div className="login-page">
      <div className="login-container">
        <div className="login-card">
          <div className="login-header">
            <h1 onClick={()=> navigate("/")}><span className="brand-dot" /> Fitness App</h1>
          </div>
          <div className="buttons flex gap-3 w-full mb-4">
            <button
              type="button"
              className={`flex-1 py-2 rounded tab-button ${activeTab === 'login' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('login');
                navigate('/login');
              }}
            >
              Login
            </button>

            <button
              type="button"
              className={`flex-1 py-2 rounded tab-button ${activeTab === 'register' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('register');
                navigate('/register');
              }}
            >
              Register
            </button>
          </div>
          <form onSubmit={handleSubmit} className="login-form">
            <div className="form-group">
              <label>Email</label>
              <input
                type="text"
                value={email}
                onChange={e => setEmail(e.target.value)}
                placeholder="example@gmail.com"
                required
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label>Password</label>
              <input
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                placeholder="••••••••"
                required
                disabled={loading}
              />
            </div>

            {error && <div className="error-message">{error}</div>}

            <button type="submit" className="tab-button bg-blue-500 text-white py-2 rounded" disabled={loading}>
              {loading ? 'Logging in…' : 'Login'}
            </button>
          </form>
          <button onClick={loginOauth}
              type="button" 
              className=" border border-transparent bg-[#1d4ed8] text-white
              transition-transform duration-200 hover:-translate-y-px py-2 rounded
              flex items-center justify-center gap-2 w-full mt-2"
              disabled={loading}
            >
              <img 
                  src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg" 
                  alt="Google"
                  className="w-5 h-5"
              />
              Continue with Google
          </button>

        </div>
      </div>
    </div>
  );
};

export default Login;