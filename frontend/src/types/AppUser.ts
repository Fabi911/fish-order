export type AppUser = {
	id: string
	username: string;
	role: "ADMIN" | "USER"| "GROUP1" | "GROUP2";
}