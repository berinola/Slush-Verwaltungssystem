import {Header} from '../../components/Header.jsx';
import { useEffect, useState } from "react";
import {ZubehorTable} from "./ZubehorTable.jsx";

export function Zubehor() {
    const [zubehor, setZubehor] = useState([]);

    async function loadZubehor() {
        const res = await fetch("/api/zubehor");
        if (res.ok) {
            setZubehor(await res.json());
        }
    }

    useEffect(() => {
        loadZubehor();
    }, []);

    return (
        <div>
            <Header/>
            <h2>Zubehörverwaltung</h2>
            <ZubehorTable zubehor={zubehor} loadZubehor={loadZubehor}/>

        </div>
    );
}
