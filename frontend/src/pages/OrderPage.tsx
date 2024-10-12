import OrderForm from "../components/OrderForm.tsx";
import './OrderPage.css';
import {Link} from "react-router-dom";
export default function OrderPage() {
	return (
		<>
			<h1>Bestellung Forellenverkauf</h1>
			<article className="article">
				<h2>für den 22.12.2024</h2>
				<h3>Preise:</h3>
				<p className="price">geräucherte Forelle: 7,50€</p>
				<p>eingelegte Forelle: 6,00€</p>
			</article>
			<OrderForm />
			<br/>
			<Link to="/order-overview">Zur Übersicht</Link>
		</>
	)
}