import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import SessionChecker from './components/SessionChecker';
import { UserProvider } from './context/UserContext';
import Home from "./pages/Home";
import JobDetail from "./pages/JobDetail";
import Jobs from "./pages/Jobs";
import Login from './pages/Login';
import Profile from './pages/Profile';
import Recommend from './pages/Recommend';
import Register from './pages/Register';
import Results from './pages/Results';
import Report from './pages/Report';

// import "./server";

export default function App() {
  return (
    <BrowserRouter>
      <SessionChecker />
      <UserProvider>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/signup" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/jobs" element={<Jobs />} />
        <Route path="/jobs/:id" element={<JobDetail />} />
        <Route path="/results" element={<Results />}></Route>
        <Route path="/recommend" element={<Recommend />}/>
        <Route path="/profile" element={<Profile />}/>
        <Route path="/report" element={<Report />}/>
      </Routes>
      </UserProvider>
    </BrowserRouter>
  );
}
