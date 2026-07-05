// import { useState } from 'react'
// import './App.css'
import { useEffect } from 'react'
import { Button } from '@mui/material'
import ActivityDetail from './components/ActivityDetail'
import ActivityForm from './components/ActivityForm'
import { ActivityList } from './components/ActivityList'
import { Navigate, BrowserRouter, Route, Routes } from 'react-router-dom'
import { Box } from '@mui/material'
import Login from './pages/Login'
import Register from './pages/Register'

const token = false;
const ActivityPage = () => {
  return (<Box  sx={{ p: 2, border: '1px dashed grey' }}> 
    <ActivityForm onActivitiesAdded={()=> {window.location.reload()}}/>
    <ActivityList/>
  </Box>)
}
function App() {
  // const [count, setCount] = useState(0)

  return (
    <BrowserRouter>
      {/* (!token ? (
        <Button variant="contained" onClick ={ ()=> {}}>
          Login
        </Button>  
      ) : ( */}
        <div>
          {/* <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
            <Button variant="contained" onClick ={ ()=> {}}>
              Logout
            </Button>  */}
            <Routes>
              <Route path = "/" element={token ? <Navigate to="/activities" replace />
                                          : <Login /> }/>
              <Route path = "/register" element={<Register /> }/>
              <Route path = "/login" element={<Login /> }/>
              <Route path = "/activities" element = {<ActivityPage/>}/>
              <Route path = "/activities/:id" element = {<ActivityDetail/>}/>
            </Routes> 
          {/* </Box> */}
        </div>
      {/* )) */}
  
    </BrowserRouter>
  )
}

export default App
