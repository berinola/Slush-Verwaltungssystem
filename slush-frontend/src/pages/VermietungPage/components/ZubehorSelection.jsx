export function ZubehorSelection({zubehor, selectedZubehor,setSelectedZubehor,setMenge,setZid, zid, menge}){
    return(
        <>
                <select value={zid} onChange={(e) => setZid(e.target.value)}>
                    <option>-- Zubehör auswählen --</option>
                    {zubehor.map(z => (
                        <option key={z.zid} value={z.zid}>{z.name}</option>
                    ))}
                </select>
                <input type="number" min="1" value={menge} onChange={e => setMenge(e.target.value)}/>
                <button onClick={() => {
                    if (!zid) return;
                    const z = zubehor.find(x => x.zid === Number(zid));
                    setSelectedZubehor([...selectedZubehor, {zid: Number(zid), menge: Number(menge), name: z.name}]);
                    setZid('');
                    setMenge(1);
                }}>
                    Zubehör hinzufügen
                </button>

            <ul>
                {selectedZubehor.map((item, i) => (
                    <li key={i}>
                        {item.name} — {item.menge} Stk.
                    </li>
                ))}
            </ul>
        </>
    );
}