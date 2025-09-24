export function MaschineTable({loadMaschinen, maschinen}){

    async function deleteMaschine(id) {
        const res = await fetch(`/api/maschine/${id}`, { method: "DELETE" });
        if (res.ok) {
            loadMaschinen();
        }else{
            alert(await res.text());
        }
    }
    return(<table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Modell</th>
            <th>Anzahl Tanks</th>
            <th>Aktionen</th>
        </tr>
        </thead>
        <tbody>
        {maschinen.map((m) => (
            <tr key={m.mid}>
                <td>{m.mid}</td>
                <td>{m.modell}</td>
                <td>{m.anzahlTanks}</td>
                <td>
                    <button onClick={() => deleteMaschine(m.mid)}>Löschen</button>
                </td>
            </tr>
        ))}
        </tbody>
    </table>);
}