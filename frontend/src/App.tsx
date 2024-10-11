import {useState} from 'react';
import './App.css';
import axios from 'axios';
import {Order} from "./types/Order.ts";

function App() {
	const [firstname, setFirstname] = useState<string>('')
	const [lastname, setLastname] = useState<string>('')
	const [email, setEmail] = useState<string>('')
	const [quantitySmoked, setQuantitySmoked] = useState<number>(0)
	const [quantityFresh, setQuantityFresh] = useState<number>(0)
	console.log("quantitySmoked: ", quantitySmoked);
	const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
		e.preventDefault();
		const order = {
			firstname,
			lastname,
			email,
			quantitySmoked,
			quantityFresh
		}
		postOrder(order);
		e.currentTarget.reset();
	}
	const postOrder = (order: Order): void => {
		console.log("order: ", order);
		axios.post('/api/orders', {
			firstname: order.firstname,
			lastname: order.lastname,
			email: order.email,
			quantitySmoked: order.quantitySmoked,
			quantityFresh: order.quantityFresh,
		})
			.then(response => {
				console.log(response);
				setFirstname('');
				setLastname('');
				setEmail('');
				setQuantitySmoked(0);
				setQuantityFresh(0);
			})
			.catch(error => {
				console.error(error);
			});
	}
	return (
		<>
			<img src='../public/logo.png' alt="fv-leineck" width="150"/>
			<h1>Bestellung Forellenverkauf</h1>
			<article className="article">
				<h2>für den 22.12.2024</h2>
				<h3>Preise:</h3>
				<p className="price">geräucherte Forelle: 8,00€</p>
				<p>eingelegte Forelle: 6,50€</p>
			</article>
			<form className="form" onSubmit={handleSubmit}>
				<div className="inputField">
					<label className="label" htmlFor="firstname">Vorname:</label>
					<input type="text" id="firstname" placeholder="Vorname" required value={firstname}
					       onChange={(e) => setFirstname(e.target.value)}/>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="lastname">Nachname:</label>
					<input type="text" id="lastname" placeholder="Nachname" required value={lastname}
					       onChange={(e) => setLastname(e.target.value)}/>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="email">Email:</label>
					<input type="email" id="email" name="email" placeholder="info@muster.com" required value={email}
					       onChange={(e) => setEmail(e.target.value)}/>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="quantity_smoked">geräucherte Forelle:</label>
					<input type="number" id="quantity_smoked" required value={quantitySmoked}
					       onChange={(e) => setQuantitySmoked(Number(e.target.value))}/>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="quantity_fresh">eingelegte Forelle:</label>
					<input type="number" id="quantity_fresh" required value={quantityFresh}
					       onChange={(e) => setQuantityFresh(Number(e.target.value))}/>
				</div>
				<button type="submit">Bestellen</button>
			</form>
		</>
	)
}

export default App