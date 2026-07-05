import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Register.css';

const Register = () => {
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [activeTab, setActiveTab] = useState('register');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const prefixUrl = 'http://localhost:8080';
    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const res = await fetch(`/${prefixUrl}/api/auth/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },  // json key should mathc with java filedname
                body: JSON.stringify({ firstName:firstname,lastName:lastname,email:email, password:password }),
            });
            const data = await res.json();

            if (res.ok) {
                navigate('/activities');
            } else {
                setError(data.error || 'Registration failed');
            }
        } catch (err) {
            setError('Network error. Please try again.');
        } finally {
            setLoading(false);
        }
    }
    return (
        <div className="register-page">
            <div className="register-container">
                <div className="register-card">
                    <div className="register-header">
                        <h1 onClick={()=> navigate("/")}><span className="brand-dot" /> Fitness App</h1>
                    </div>
                    <div className="buttons flex gap-3 w-full mb-4">
                        <button
                            type="button"
                            className={`flex-1 py-2 rounded tab-button ${activeTab === 'login' ? 'active' : ''}`}
                            onClick={() =>{
                                setActiveTab('login')
                                 navigate("/login")
                            }}
                        >
                            Login
                        </button>

                        <button
                            type="button"
                            className={`flex-1 py-2 rounded tab-button ${activeTab === 'register' ? 'active' : ''}`}
                            onClick={() => {
                                setActiveTab('register')
                                navigate("/register")}}     
                        >
                            Register
                        </button>
                    </div>
                    <form onSubmit={handleSubmit} className="register-form">
                        <div className="form-group">  <label>Firstname</label>
                            <input
                                type="text"
                                value={firstname}
                                onChange={e => setFirstname(e.target.value)}
                                placeholder="ex: ajay"
                                required
                                disabled={loading}
                            />
                        </div>
                        <div className="form-group">
                            <label>Lastname</label>
                            <input
                                type="text"
                                value={lastname}
                                onChange={e => setLastname(e.target.value)}
                                placeholder="ex: kumar"
                                required
                                disabled={loading}
                            />
                        </div>
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

                        <button type="submit" className=" tab-button bg-blue-500 text-white py-2 rounded" disabled={loading}>
                            {loading ? 'Registering..' : 'Register'}
                        </button>
                    </form>
                    <button 
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

export default Register;