--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5 (Debian 17.5-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: kunde; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kunde (
    kid integer NOT NULL,
    fname text,
    lname text,
    mail text,
    telefon character varying(20),
    CONSTRAINT kunde_mail_check CHECK ((mail ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'::text))
);


ALTER TABLE public.kunde OWNER TO postgres;

--
-- Name: kunde_kid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.kunde ALTER COLUMN kid ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.kunde_kid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: maschine; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.maschine (
    mid integer NOT NULL,
    modell character varying(50),
    anzahl_tanks integer,
    CONSTRAINT maschine_anzahltanks_check CHECK (((anzahl_tanks > 0) AND (anzahl_tanks < 3)))
);


ALTER TABLE public.maschine OWNER TO postgres;

--
-- Name: maschine_mid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.maschine ALTER COLUMN mid ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.maschine_mid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: vermietung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vermietung (
    vid integer NOT NULL,
    kid integer,
    mid integer,
    start timestamp without time zone,
    ende timestamp without time zone,
    CONSTRAINT vermietung_check CHECK ((start < ende))
);


ALTER TABLE public.vermietung OWNER TO postgres;

--
-- Name: vermietung_vid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.vermietung ALTER COLUMN vid ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.vermietung_vid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: vermietung_zubehor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vermietung_zubehor (
    zid integer NOT NULL,
    vid integer NOT NULL,
    menge integer
);


ALTER TABLE public.vermietung_zubehor OWNER TO postgres;

--
-- Name: zubehor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zubehor (
    zid integer NOT NULL,
    typ text,
    name text,
    anzahl integer,
    preis double precision,
    CONSTRAINT zubehor_preis_check CHECK ((preis > (0)::double precision))
);


ALTER TABLE public.zubehor OWNER TO postgres;

--
-- Name: zubehor_zid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.zubehor ALTER COLUMN zid ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.zubehor_zid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: kunde kunde_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kunde
    ADD CONSTRAINT kunde_pkey PRIMARY KEY (kid);


--
-- Name: maschine maschine_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maschine
    ADD CONSTRAINT maschine_pkey PRIMARY KEY (mid);


--
-- Name: vermietung vermietung_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vermietung
    ADD CONSTRAINT vermietung_pkey PRIMARY KEY (vid);


--
-- Name: vermietung_zubehor vermietung_zubehor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vermietung_zubehor
    ADD CONSTRAINT vermietung_zubehor_pkey PRIMARY KEY (zid, vid);


--
-- Name: zubehor zubehor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zubehor
    ADD CONSTRAINT zubehor_pkey PRIMARY KEY (zid);


--
-- Name: vermietung vermietung_kid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vermietung
    ADD CONSTRAINT vermietung_kid_fkey FOREIGN KEY (kid) REFERENCES public.kunde(kid);


--
-- Name: vermietung vermietung_mid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vermietung
    ADD CONSTRAINT vermietung_mid_fkey FOREIGN KEY (mid) REFERENCES public.maschine(mid);


--
-- Name: vermietung_zubehor vermietung_zubehor_vid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vermietung_zubehor
    ADD CONSTRAINT vermietung_zubehor_vid_fkey FOREIGN KEY (vid) REFERENCES public.vermietung(vid);


--
-- Name: vermietung_zubehor vermietung_zubehor_zid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vermietung_zubehor
    ADD CONSTRAINT vermietung_zubehor_zid_fkey FOREIGN KEY (zid) REFERENCES public.zubehor(zid);


--
-- PostgreSQL database dump complete
--

