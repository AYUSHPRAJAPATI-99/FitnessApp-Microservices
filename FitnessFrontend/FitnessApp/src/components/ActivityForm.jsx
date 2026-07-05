import { Box, Button, FormControl, TextField } from '@mui/material'
import React, { useState } from 'react'
import { addActivity } from '../services/api';

export default function ActivityForm({onActivitiesAdded}) {
    const[activity, setActivity] = useState({
        type:"running", duration:"", calorieBurned:'',
        additionalMatrices : {}
    })
    const handleSubmit = async (e) => {
        e.preventDefault();
        try{ 
            // activity usi type ka format jaise api ko chahiye
            await addActivity(activity); // from api we define
            onActivitiesAdded();
            setActivity({
                type:"running", duration:"", calorieBurned:'',
                additionalMatrices : {}
            })
        } catch (error){
            console.log(error);
        }
    }
  return (
     <Box component="form" sx={{ p: 2, border: '1px dashed grey' }} onSubmit={handleSubmit}>
        <FormControl >
        <InputLabel >Activity Type</InputLabel>
        <Select
            value={activity.type}
            label="Age"
            onChange={(e)=> {setActivity({...activity,type:e.target.value})}}
        >
            <MenuItem value="RUNNING">Running</MenuItem>
            <MenuItem value="WALKING">Walking</MenuItem>
            <MenuItem value="CYCLING">Cycling</MenuItem>
        </Select>
        </FormControl>
        <TextField fullWidth
        label = "Calorie Burned"
        type = "number"
        sx = {{mb:2}}
        value = {activity.calorieBurned}
        onChange={(e) => {setActivity({...activity,calorieBurned:e.target.value})}} />

        <TextField fullWidth
        label = "Duration"
        type = "number"
        sx = {{mb:2}}
        value = {activity.duration}
        onChange={(e) => {setActivity({...activity,duration:e.target.value})}} />
        <Button type = "submit" variant='container'> Add Activity </Button>
    </Box>
  )
}
