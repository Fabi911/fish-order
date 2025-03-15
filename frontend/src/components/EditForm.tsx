import axios from "axios";
import {useState} from "react";
import {Order} from "../types/Order.ts";
import './OrderForm.css';
import AddBoxIcon from '@mui/icons-material/AddBox';
import IndeterminateCheckBoxIcon from '@mui/icons-material/IndeterminateCheckBox';
import {useNavigate} from "react-router-dom";
import {toast} from "react-toastify";

interface Props {
	order: Order;
}

export default function EditForm({order}: Props) {
	const navigate = useNavigate();
	const id:string | undefined=order.id;
	const [firstname, setFirstname] = useState<string>(order.firstname);
	const [lastname, setLastname] = useState<string>(order.lastname);
	const [email, setEmail] = useState<string>(order.email);
	const [phone, setPhone]=useState<string>(order.phone);
	const [quantitySmoked, setQuantitySmoked] = useState<number>(order.quantitySmoked);
	const [quantityFresh, setQuantityFresh] = useState<number>(order.quantityFresh);
	const [pickupPlace, setPickupPlace] = useState<string>(order.pickupPlace);
	const [comment, setComment] = useState<string>(order.comment);
	const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
		e.preventDefault();
		if (!pickupPlace) {
			alert("Bitte wählen Sie einen Abholort aus, bevor Sie die Bestellung absenden.");
			return;
		}
		const order = {
			id,
			firstname,
			lastname,
			email,
			phone,
			pickupPlace,
			comment,
			quantitySmoked,
			quantityFresh,
			pickedUp: false
		}
		updateOrder(order,e);
		navigate('/order-overview')

	}

	// Update order
	const updateOrder = (order: Order , e: React.FormEvent<HTMLFormElement>): void => {
		toast.promise(
		axios.put(`/api/orders/${order.id}`, {
			firstname: order.firstname,
			lastname: order.lastname,
			email: order.email,
			phone: order.phone,
			pickupPlace: order.pickupPlace,
			comment: order.comment,
			quantitySmoked: order.quantitySmoked,
			quantityFresh: order.quantityFresh,
		})
			.then(response => {
				console.log('response: ',response);
				setFirstname('');
				setLastname('');
				setEmail('');
				setPhone('');
				setPickupPlace('');
				setComment('');
				setQuantitySmoked(0);
				setQuantityFresh(0);
				e.currentTarget?.reset();
			}),
			{
				pending: "Änderungen werden übermittelt...",
				success: "Bestellung erfolgreich bearbeitet.",
				error: "Fehlgeschlagen! Bitte versuchen Sie es später erneut. 🤯"
			})
			.catch(error => {
				console.error(error);
			});
	}

	return (
		<>
			<form className="form" onSubmit={handleSubmit}>

				<div className="inputField">
					<label className="label" htmlFor="firstname">Vorname:*</label>
					<input type="text" id="firstname" placeholder="Vorname" required value={firstname}
					       onChange={(e) => setFirstname(e.target.value)}/>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="lastname">Nachname:*</label>
					<input type="text" id="lastname" placeholder="Nachname" required value={lastname}
					       onChange={(e) => setLastname(e.target.value)}/>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="email">Email:*</label>
					<input type="email" id="email" name="email" placeholder="info@muster.com" required value={email}
					       onChange={(e) => setEmail(e.target.value)}/>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="phone">Telefonnummer:*</label>
					<input type="tel" id="phone" name="phone" placeholder="0123456789" required value={phone}
					       onChange={(e) => setPhone(e.target.value)}/>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="quantity_smoked">Geräucherte Forelle:</label>
					<div className="quantity-controls">
						<button type="button" onClick={() => setQuantitySmoked(prev => Math.max(prev - 1, 0))}>
							<IndeterminateCheckBoxIcon fontSize="large"/></button>
						<span>{quantitySmoked}</span>
						<button type="button" onClick={() => setQuantitySmoked(prev => prev + 1)}><AddBoxIcon
							fontSize="large"/></button>
					</div>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="quantity_smoked">Eingelegte Forelle:</label>
					<div className="quantity-controls">
						<button type="button" onClick={() => setQuantityFresh(prev => Math.max(prev - 1, 0))}>
							<IndeterminateCheckBoxIcon fontSize="large"/></button>
						<span>{quantityFresh}</span>
						<button type="button" onClick={() => setQuantityFresh(prev => prev + 1)}><AddBoxIcon
							fontSize="large"/></button>
					</div>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="pickupPlace">Abholort:*</label>
					<select id="pickupPlace" required value={pickupPlace}
					        onChange={(e) => setPickupPlace(e.target.value)}>
						<option value="">Bitte wählen</option>
						<option value="Weinstadt (Tanja & Ralph)">Weinstadt (Tanja & Ralph)</option>
						<option value="Vereinsheim am Leinecksee">Vereinsheim am Leinecksee</option>
					</select>
				</div>
				<div className="inputField">
					<label className="label" htmlFor="comment">Kommentar:</label>
					<textarea id="comment" placeholder="Kommentar" value={comment}
					          onChange={(e) => setComment(e.target.value)}/>
				</div>
				<button className="orderButton" type="submit">Ändern</button>
			</form>
		</>
	)
}