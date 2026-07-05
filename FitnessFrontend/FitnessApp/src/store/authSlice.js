import { createSlice } from '@reduxjs/toolkit'



const authSlice = createSlice({
  name: 'auth',
  initialState :{          // from local storage
    user:  JSON.parse(localStorage.getItem('user')) || null, 
    token: localStorage.getItem('token') || null , 
    userId: localStorage.getItem('userId') || null , 
  },
  reducers: {
    setCredentials: (state) =>{
      
    },
    logout: (state) => {
      
    },
  },
})

export const { setCredentials, logout } = authSlice.actions
export default authSlice.reducer
// redux toolkit authslice to  convert kr raha object ko jisme key present hai actions, reducer jo hum use kr rahe
