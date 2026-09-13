export interface BalanceProps {
    balance: number
    status: string | undefined
    currency: string | undefined
}

export default function BallanceCard({balance, status, currency}: BalanceProps) {
    return(
        <div className="md:col-span-1 bg-linear-to-br from-slate 800 to-slate-900
        border border-slate-700 rounded-xl p-6 shadow-lg flex flex-col justify-between">
            <div>
                <span className="text-xs uppercase tracking-wider text-slate-400 
                font-semibold">
                    Saldo disponível
                </span>
                <div className="text-3xl font-extrabold text-emerald-400 mt-2">
                    {balance.toFixed(2)}
                    <span className="text-sm font-normal text-slate-300 px-1">
                        {currency}
                    </span>
                </div>
            </div>
            <div className="mt-6 pt-4 border-t border-slate-800 text-xs
            text-slate-400 space-y-1">
                <p>Status: <span>{status}</span></p>
            </div>
        </div>
    )
}