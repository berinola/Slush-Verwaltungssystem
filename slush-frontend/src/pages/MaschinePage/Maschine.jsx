import {Header} from '../../components/Header.jsx';
import { useEffect, useState } from "react";
import {MaschineTable} from "./MaschineTable.jsx";

export function Maschine() {
    const [maschinen, setMaschinen] = useState([]);
    const [form, setForm] = useState({ modell: "", anzahlTanks: "" });

    async function loadMaschinen() {
        const res = await fetch("/api/maschine");
        if (res.ok) {
            setMaschinen(await res.json());
        }
    }

    useEffect(() => {
        loadMaschinen();
    }, []);

    function handleChange(e) {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    }

    async function saveMaschine() {
        if (!form.modell || !form.anzahlTanks) return;

        const res = await fetch("/api/maschine", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({modell: form.modell, anzahlTanks: Number(form.anzahlTanks)}),
        });

        if (res.ok) {
            setForm({modell: "", anzahlTanks: ""});
            loadMaschinen();
        } else {
            alert(await res.text());
        }
    }

    return (
        <div>
            <Header/>
            <h2>Maschinen</h2>

            <div>
                <input
                    type="text"
                    name="modell"
                    placeholder="Modell"
                    value={form.modell}
                    onChange={handleChange}
                />
                <input
                    type="number"
                    name="anzahlTanks"
                    placeholder="Anzahl Tanks"
                    value={form.anzahlTanks}
                    onChange={handleChange}
                    min="1"
                />
                <button onClick={saveMaschine}>Hinzufügen</button>
            </div>
            <MaschineTable loadMaschinen={loadMaschinen} maschinen={maschinen}/>
        </div>
    );
}
