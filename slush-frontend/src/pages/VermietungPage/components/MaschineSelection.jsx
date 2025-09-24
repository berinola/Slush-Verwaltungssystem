
export function MaschineSelection({maschineId, maschinen, setMaschineId}) {

    return (
        <>
            <select value={maschineId} onChange={(e) => {
                setMaschineId(e.target.value)
            }}>
                <option value="">— Maschine wählen —</option>
                {maschinen.map(m => (
                    <option key={m.mid} value={m.mid}>{m.modell}</option>
                ))}
            </select>
        </>

    );
}