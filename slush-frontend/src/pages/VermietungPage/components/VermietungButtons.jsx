export function VermietungButtons({getVermietungen,getVermietungFurZeitraum, postVermietung}){


    return(<>
        <div>
            <button onClick={getVermietungen}>Alle Anzeigen</button>
            <button onClick={getVermietungFurZeitraum}>Zeitraum Anzeigen</button>
            <button onClick={postVermietung}>Hinzufügen</button>
        </div>
        </>);
}