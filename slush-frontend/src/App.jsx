import { useState } from 'react'
import {Routes, Route} from 'react-router-dom'
import {Vermietung} from "./pages/VermietungPage/Vermietung.jsx";
import './App.css'
import {Maschine} from "./pages/MaschinePage/Maschine.jsx";
import {Kunden} from "./pages/KundenPage/Kunden.jsx";
import {Zubehor} from "./pages/ZubehorPage/Zubehor.jsx";

function App() {

  return (
    <>
        <Routes>
            <Route path="/" element={<Vermietung/>} />
            <Route path={"kunden"} element={<Kunden/>}/>
            <Route path={"maschine"} element={<Maschine/>}/>
            <Route path={"zubehor"} element={<Zubehor/>}/>
        </Routes>
    </>
  )
}

export default App
