import OrderForm from "../components/OrderForm.tsx";
import './OrderPage.css';
export default function OrderPage() {
	return (
		<>
			<h1>Bestellung Forellenverkauf</h1>
			<article className="article">
				<h2>für den 22.12.2024</h2>
				<h3>Preise:</h3>
				<p className="price">geräucherte Forelle: 8,00€</p>
				<p>eingelegte Forelle: 6,50€</p>
			</article>
			<OrderForm />
		</>
	)
}