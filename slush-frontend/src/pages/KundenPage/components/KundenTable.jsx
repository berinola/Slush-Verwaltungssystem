export function KundenTable({kunden, editKunde}){
    return(<table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Vorname</th>
            <th>Nachname</th>
            <th>E-Mail</th>
            <th>Telefon</th>
            <th>Aktionen</th>
        </tr>
        </thead>
        <tbody>
        {kunden.map((k) => (
            <tr key={k.kid}>
                <td>{k.kid}</td>
                <td>{k.fname}</td>
                <td>{k.lname}</td>
                <td>{k.mail}</td>
                <td>{k.telefon}</td>
                <td>
                    <button onClick={() => editKunde(k)}>Bearbeiten</button>
                </td>
            </tr>
        ))}
        </tbody>
    </table>);
}