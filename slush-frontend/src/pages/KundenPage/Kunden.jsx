import {Header} from '../../components/Header.jsx';
import { useEffect, useState } from "react";
import {KundenTable} from "./components/KundenTable.jsx";
import {KundenFormular} from "./components/KundenFormular.jsx";

export function Kunden() {
    const [kunden, setKunden] = useState([]);
    const [form, setForm] = useState({ kid: "", fname: "", lname: "", mail: "", telefon: "" });
    const [editMode, setEditMode] = useState(false);

    async function loadKunden() {
        const res = await fetch("/api/kunden");
        if (res.ok) {
            setKunden(await res.json());
        }else{
            alert(res.text());
        }
    }

    useEffect(() => {
        loadKunden();
    }, []);

    function editKunde(k) {
        setForm(k);
        setEditMode(true);
    }

    return (
        <>
        <Header/>
            <h2>Kundenverwaltung</h2>
            <KundenFormular loadKunden={loadKunden} form={form} setForm={setForm} editMode={editMode} setEditMode={setEditMode}/>
            <KundenTable kunden={kunden} editKunde={editKunde}/>
        </>
    );
}
