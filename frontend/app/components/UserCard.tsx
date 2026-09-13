export interface UserProps {
    id: string
    nome: string
    email: string
    cpf: string
    fetchWallet: (id: string) => void | Promise<void>
}


export default function UserCard({id, nome, email, cpf, fetchWallet}: UserProps) {
    return(
        <button className=" group bg-slate-800/60
        border border-slate-700 rounded-xl p-6 shadow-lg hover:border-emerald-400 focus:bg-slate-300"
        onClick={() => fetchWallet(id)}
        >
                <p className="text-md uppercase trackingroup-focus:text-slate-950g-wider text-slate-100 
                font-semibold group-focus:text-emerald-900">
                    {nome}
                </p>
                <p className="text-xs tracking-wider text-slate-400 
                 group-focus:text-emerald-900">
                    {email}
                </p>
                <p className="text-xs uppercase tracking-wider text-slate-400 
                group-focus:text-emerald-900">
                    {cpf}
                </p>
                
                
           
        </button>
    )
}