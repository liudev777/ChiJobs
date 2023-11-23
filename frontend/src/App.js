import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import SessionChecker from './components/SessionChecker';
import Home from "./pages/Home";
import JobDetail from "./pages/JobDetail";
import Jobs from "./pages/Jobs";
import Login from './pages/Login';
import Register from './pages/Register';
import Results from './pages/Results';

// import "./server";

export default function App() {
  return (
    <BrowserRouter>
      <SessionChecker />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/signup" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/jobs" element={<Jobs />} />
        <Route path="/jobs/:id" element={<JobDetail />} />
        <Route path="/results" element={<Results />}></Route>
      </Routes>
    </BrowserRouter>
  );
}
