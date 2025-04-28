import OrderForm from "../components/OrderForm.tsx";
import './OrderPage.css';
import "react-toastify/dist/ReactToastify.css";

export default function OrderPage() {
	return (
		<>
			<h1 className="orderPage">Bestellung Forellenverkauf</h1>
			<article className="article">
				<h2>für Fronleichnam, den 19.06.2025</h2>
				<h3>Preise:</h3>
				<p className="price">geräucherte Forelle: 7,50€</p>
				<p>eingelegte Forelle: 6,00€</p>
			</article>
			<OrderForm />
			<br/>
		</>
	)
}