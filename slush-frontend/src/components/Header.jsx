import {NavLink, Link} from "react-router";
import "./Header.css"
export function Header() {
    return (
        <div className="header">
            <div><NavLink to="/">Vermietung</NavLink></div>
            <div><NavLink to="/kunden">Kunden</NavLink></div>
            <div><NavLink to="/zubehor">Zubehör</NavLink></div>
            <div><NavLink to="/maschine">Maschine</NavLink></div>
        </div>
    );
}