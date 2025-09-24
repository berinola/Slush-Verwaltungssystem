import {Header} from '../../components/Header.jsx';
import {useEffect, useState} from "react";
import {KundeSelection} from "./components/KundeSelection.jsx";
import {MaschineSelection} from "./components/MaschineSelection.jsx";
import {VermietungTable} from "./components/VermietungTable.jsx";
import {VermietungButtons} from "./components/VermietungButtons.jsx";
import {ZubehorSelection} from "./components/ZubehorSelection.jsx";

export function Vermietung() {

    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [kunden, setKunden] = useState([]);
    const [maschinen, setMaschinen] = useState([]);
    const [zubehor, setZubehor] = useState([]);

    const [kundeId, setKundeId] = useState('');
    const [maschineId, setMaschineId] = useState('');


    const [zid, setZid] = useState('');
    const [menge, setMenge] = useState(1);
    const [selectedZubehor, setSelectedZubehor] = useState([]);

    const [vermietungen, setVermietungen] = useState([]);

    useEffect(() => {
        fetch("/api/kunden").then(res => res.json()).then(setKunden);
        fetch("/api/maschine").then(res => res.json()).then(setMaschinen);
        fetch("/api/zubehor").then(res => res.json()).then(setZubehor);
    }, []);

    async function getVermietungen() {
        const res = await fetch("/api/vermietung");
        if (!res.ok) {
            alert(await res.text());
        } else {
            setVermietungen(await res.json());
        }
    }

    async function getVermietungFurZeitraum() {

        const res = await fetch(`/api/vermietung/${startDate}/${endDate}`);
        if (res.ok) {
            setVermietungen(await res.json());
        }

    }

    async function openInvoicePdf(vid) {
        const res = await fetch(`/api/vermietung/${vid}/rechnung.pdf`);
        if (!res.ok) {
            alert('Rechnung konnte nicht erstellt werden.');
            return;
        }
        const blob = await res.blob();
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `rechnung-${vid}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
    }

    async function postVermietung() {
        if (!startDate || !endDate || !kundeId || !maschineId) {
            alert('Bitte Start/Ende sowie Kunde und Maschine auswählen.');
            return;
        }
        const withSeconds = v => v && v.length === 16 ? `${v}:00` : v;

        let neuVermietung = {
            start: withSeconds(startDate),
            ende: withSeconds(endDate),
            kunde: {kid: Number(kundeId)},
            maschine: {mid: Number(maschineId)}
        };

        const res = await fetch('/api/vermietung', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(neuVermietung)
        });

        if (res.ok) {

            const created = await res.json();

            for (const item of selectedZubehor) {
                const resZ = await fetch(`/api/vermietung/${created.vid}/zubehor/${item.zid}?menge=${item.menge}`, {
                    method: 'POST'
                });
                if (!resZ.ok) {
                    alert(await resZ.text());
                }
            }

            const k = kunden.find(x => x.kid === created.kunde.kid);
            const m = maschinen.find(x => x.mid === created.maschine.mid);
            setVermietungen([...vermietungen, {
                ...created,
                kunde: k,
                maschine: m
            }]);

            setSelectedZubehor([]);
            setZid('');
            setMenge(1);
            await openInvoicePdf(created.vid);
        }

    }

    return (
        <div>
            <Header/>
            <h2>Vermietungverwaltung</h2>
            <div>
                <KundeSelection kundeId={kundeId} kunden={kunden} setKundeId={setKundeId}/>
                <MaschineSelection maschineId={maschineId} maschinen={maschinen} setMaschineId={setMaschineId}/>
                <input type="datetime-local"
                       value={startDate}
                       onChange={(e) => setStartDate(e.target.value)}
                />
                <input type="datetime-local"
                       value={endDate}
                       onChange={(e) => setEndDate(e.target.value)}
                />
            </div>

            <ZubehorSelection zubehor={zubehor} selectedZubehor={selectedZubehor}
                              setSelectedZubehor={setSelectedZubehor} menge={menge} setMenge={setMenge} setZid={setZid}
                              zid={zid}/>

            <VermietungButtons getVermietungen={getVermietungen}
                               getVermietungFurZeitraum={getVermietungFurZeitraum}
                               postVermietung={postVermietung}/>

            <VermietungTable vermietungen={vermietungen}/>
        </div>
    );
}
