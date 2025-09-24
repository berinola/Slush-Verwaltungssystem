import {useState} from "react";

export function VermietungTable({vermietungen}) {
    const [openVid, setOpenVid] = useState(null);
    const [zubehore, setZubehore] = useState({}); // Map: vid -> array

    function formatDateTime(isoString) {
        const d = new Date(isoString);
        const date = d.toLocaleDateString("de-DE", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric"
        });
        const time = d.toLocaleTimeString("de-DE", {
            hour: "2-digit",
            minute: "2-digit"
        });
        return `${date} ${time} Uhr`;
    }

    async function getVermietungZubehore(vId) {
        try {
            const res = await fetch(`/api/vermietung/${vId}/zubehor`);
            if (!res.ok) {
                alert(await res.text());
                return;
            }
            const data = await res.json();

            setZubehore({...zubehore, [vId]: data } );
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <table>
            <thead>
            <tr>
                <th>Kunde</th>
                <th>Maschine</th>
                <th>Startdatum</th>
                <th>Enddatum</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            {vermietungen.map(v => (
                <tr key={v.vid}>
                    <td>{v.kunde.lname}-{v.kunde.fname}</td>
                    <td>{v.maschine.modell}</td>
                    <td>{formatDateTime(v.start)}</td>
                    <td>{formatDateTime(v.ende)}</td>
                    <td>
                        {openVid === v.vid ? (
                            <>
                                <button onClick={() => setOpenVid(null)}>
                                    Zubehör schließen
                                </button>
                                <table>
                                    <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Typ</th>
                                        <th>Menge</th>
                                        <th>Preis</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {(zubehore[v.vid] || []).map(vz => (
                                        <tr key={`${vz.id.vid}-${vz.id.zid}`}>
                                            <td>{vz.zubehor.name}</td>
                                            <td>{vz.zubehor.typ}</td>
                                            <td>{vz.menge}</td>
                                            <td>{vz.zubehor.preis} €</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </>
                        ) : (
                            <button onClick={() => {
                                setOpenVid(v.vid);

                                if (!zubehore[v.vid]) {
                                    getVermietungZubehore(v.vid);
                                }
                            }}>
                                Zubehör anzeigen
                            </button>
                        )}
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}