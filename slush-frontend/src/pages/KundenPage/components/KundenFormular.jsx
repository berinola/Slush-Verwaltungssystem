export function KundenFormular({editMode, setForm,setEditMode,loadKunden,form}){
    function handleChange(e) {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    }

    async function saveKunde() {
        const method = editMode ? "PUT" : "POST";
        const res = await fetch("/api/kunden", {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(form),
        });
        if (res.ok) {
            setForm({ kid: "", fname: "", lname: "", mail: "", telefon: "" });
            setEditMode(false);
            loadKunden();
        }else{
            alert(res.text());
        }
    }

    return(<div>
        <input
            type="text"
            name="fname"
            placeholder="Vorname"
            value={form.fname}
            onChange={handleChange}
        />
        <input
            type="text"
            name="lname"
            placeholder="Nachname"
            value={form.lname}
            onChange={handleChange}
        />
        <input
            type="email"
            name="mail"
            placeholder="E-Mail"
            value={form.mail}
            onChange={handleChange}
        />
        <input
            type="text"
            name="telefon"
            placeholder="Telefon"
            value={form.telefon}
            onChange={handleChange}
        />
        <button onClick={saveKunde}>{editMode ? "Aktualisieren" : "Hinzufügen"}</button>
        {editMode && (
            <button
                onClick={() => {
                    setForm({ kid: "", fname: "", lname: "", mail: "", telefon: "" });
                    setEditMode(false);
                }}
            >
                Abbrechen
            </button>
        )}
    </div>);
}