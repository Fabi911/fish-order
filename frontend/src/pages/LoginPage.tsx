import {FormEvent, useState} from "react";
import styled from "@emotion/styled";

type LoginPageProps = {
	login: (username: string, password: string) => void;
}
export default function LoginPage(porps: LoginPageProps) {
	const [username, setUsername] = useState<string>("");
	const [password, setPassword] = useState<string>("");

	function handleSubmit(e: FormEvent<HTMLFormElement>) {
		e.preventDefault();
		porps.login(username, password);
	}

	return (
		<>
			<Form onSubmit={handleSubmit}>
				<input type="text" placeholder="Username" value={username}
				       onChange={(e) => setUsername(e.target.value)}/>
				<input type="password" placeholder="Password" value={password}
				       onChange={(e) => setPassword(e.target.value)}/>
				<button className="exportButton" type="submit">Login</button>
			</Form>
			<article> {/*für Bestellprozess deaktivieren*/}
				<p>Vielen Dank für Ihre Bestellungen!<br/>
				Es können keine weiteren Bestellungen mehr aufgenommen werden.</p>
			<br/>
			</article>
			{/*<Link to={"/register"}>Register</Link>*/}
		</>
	)
}

// Styles

const Form = styled.form`
	display: flex;
	flex-direction: column;
	gap: 0.5rem;
	width: 200px;
	margin: 2rem auto 1rem auto;
`;