export type Order = {
	firstname: string
	lastname: string
	email: string
	phone: string
	pickupPlace: string
	comment: string
	quantitySmoked: number
	quantityFresh: number
	id?: string
	pickedUp: boolean
}