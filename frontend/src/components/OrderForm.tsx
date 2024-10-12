import axios from "axios";
import {useState} from "react";
import {Order} from "../types/Order.ts";
import './OrderForm.css';
import AddBoxIcon from '@mui/icons-material/AddBox';
import IndeterminateCheckBoxIcon from '@mui/icons-material/IndeterminateCheckBox';

export default function OrderForm() {
	const [firstname, setFirstname] = useState<string>('')
	const [lastname, setLastname] = useState<string>('')
	const [email, setEmail] = useState<string>('')
	const [quantitySmoked, setQuantitySmoked] = useState<number>(0)
	const [quantityFresh, setQuantityFresh] = useState<number>(0)
	const [responseMessage, setResponseMessage] = useState<string>('');
	const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
		e.preventDefault();
		const order = {
			firstname,
			lastname,
			email,
			quantitySmoked,
			quantityFresh
		}
		postOrder(order,e);

	}
	const postOrder = (order: Order , e: React.FormEvent<HTMLFormElement>): void => {
		axios.post('/api/orders', {
			firstname: order.firstname,
			lastname: order.lastname,
			email: order.email,
			quantitySmoked: order.quantitySmoked,
			quantityFresh: order.quantityFresh,
		})
			.then(response => {
				console.log('response: ',response);
				setFirstname('');
				setLastname('');
				setEmail('');
				setQuantitySmoked(0);
				setQuantityFresh(0);
				e.currentTarget?.reset();
				setResponseMessage('Bestellung war erfolgreich!\n Sie erhalten eine Bestätigung per E-Mail.');
			})
			.catch(error => {
				console.error(error);
				setResponseMessage('Bestellung fehlgeschlagen!');
			});
	}
	return (
		<>
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
				<label className="label" htmlFor="quantity_smoked">Geräucherte Forelle:</label>
				<div className="quantity-controls">
					<button type="button" onClick={() => setQuantitySmoked(prev => Math.max(prev - 1, 0))}><IndeterminateCheckBoxIcon fontSize="large"/></button>
					<span>{quantitySmoked}</span>
					<button type="button" onClick={() => setQuantitySmoked(prev => prev + 1)}><AddBoxIcon fontSize="large"/></button>
				</div>
			</div>
			<div className="inputField">
				<label className="label" htmlFor="quantity_smoked">Eingelegte Forelle:</label>
				<div className="quantity-controls">
					<button type="button" onClick={() => setQuantityFresh(prev => Math.max(prev - 1, 0))}><IndeterminateCheckBoxIcon fontSize="large"/></button>
					<span>{quantityFresh}</span>
					<button type="button" onClick={() => setQuantityFresh(prev => prev + 1)}><AddBoxIcon fontSize="large"/></button>
				</div>
			</div>
			<button className="orderButton" type="submit">Bestellen</button>
		</form>
			{responseMessage && <p>{responseMessage}</p>}
		</>
	)
}