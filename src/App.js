import React from 'react';
import { BrowserRouter, Link, Route, Routes } from 'react-router-dom';
import './App.css';
import Home from "./pages/Home";

export default function App() {
  return (
    <BrowserRouter>
    <Link to="/">Home</Link>
      <Routes>
        <Route path="/" element={<Home />} />
      </Routes>
    </BrowserRouter>
  );
}
