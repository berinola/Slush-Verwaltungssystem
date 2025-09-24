
export function KundeSelection({kundeId, kunden, setKundeId}) {


    return (
        <>
            <select value={kundeId} onChange={(e) => {
                setKundeId(e.target.value)
            }}>
                <option value="">— Kunde wählen —</option>
                {kunden.map(k => (
                    <option key={k.kid} value={k.kid}>{k.lname} - {k.fname}</option>
                ))}
            </select>
        </>
    );
}