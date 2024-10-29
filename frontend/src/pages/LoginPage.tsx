import {FormEvent, useState} from "react";
import {Link} from "react-router-dom";
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
				<button type="submit">Login</button>
			</Form>
			<br/>
			<Link to={"/register"}>Register</Link>
		</>
	)
}

// Styles

const Form = styled.form`
	display: flex;
	flex-direction: column;
	gap: 0.5rem;
	width: 200px;
	margin: 0 auto;
`;