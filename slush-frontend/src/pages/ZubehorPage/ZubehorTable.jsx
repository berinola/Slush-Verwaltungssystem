import {useState} from "react";

export function ZubehorTable({zubehor, loadZubehor}){
    const [eingabe, setEingabe] = useState({});

    function handleChange(zid, value) {
        setEingabe({ ...eingabe, [zid]: value });
    }

    async function addBestand(zid) {
        const menge = Number(eingabe[zid]);
        if (menge <= 0) return;

        const res = await fetch(`/api/zubehor/${zid}?anzahl=${menge}`, {
            method: "PUT",
        });
        if (res.ok) {
            setEingabe({ ...eingabe, [zid]: "" });
            loadZubehor();
        }
    }

    return (<table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Typ</th>
            <th>Aktuelle Anzahl</th>
            <th>Preis</th>
            <th>Bestand ergänzen</th>
        </tr>
        </thead>
        <tbody>
        {zubehor.map((z) => (
            <tr key={z.zid}>
                <td>{z.name}</td>
                <td>{z.typ}</td>
                <td>{z.anzahl}</td>
                <td>{z.preis}</td>
                <td>
                    <input
                        type="number"
                        value={eingabe[z.zid] || ""}
                        onChange={(e) => handleChange(z.zid, e.target.value)}
                    />
                    <button onClick={() => addBestand(z.zid)}>+</button>
                </td>
            </tr>
        ))}
        </tbody>
    </table>);
}