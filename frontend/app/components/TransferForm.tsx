import { useState } from "react"

interface TransferProps {
  email: string | undefined
  amount: number | null
  setEmail: React.Dispatch<React.SetStateAction<string>>
  setAmount: (amount: number) => void | number
  handleTransfer: (email: string) => void 
}

export default function TransferForm({email, amount, setEmail, setAmount, handleTransfer}: TransferProps) {
  const [valorFormatado, setValorFormatado] = useState<string>('0.0')

  const formatar = (valorOriginal: string) => {
    const apenasNumeros = valorOriginal.replace(/\D/g, '')

    if(!apenasNumeros) return '0.0'

    const centavos = parseInt(apenasNumeros, 10) / 100

    return String(centavos)
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const valorDigitado = e.target.value
    const formatado = formatar(valorDigitado)
    setValorFormatado(formatado)
    setAmount(Number(formatado))
  }
  return(
          <div className="grid gap-2">
            <input 
            type="text" 
            placeholder="email@email.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="flex-1 bg-slate-950 border border-slate-700 rounded-lg
            px-4 py-2.5 text-sm  text-slate-100 focus:outline-none
            focus:border-emerald-500 font-mono" 
            />
            
            <input 
            type="numeric" 
            placeholder="113.35"
            step={0.01}
            value={valorFormatado}
            onChange={handleChange}
            className="flex-1 bg-slate-950 border border-slate-700 rounded-lg
            px-4 py-2.5 text-sm  text-slate-100 focus:outline-none
            focus:border-emerald-500 font-mono" 
            />
            <button
              onClick={() => handleTransfer(email)}
              className="bg-emerald-600 hover:bg-emerald-500 text-white font-medium px-6 py-2.5 rounded-lg text-sm transition-colors disabled:opacity-50"
            >
              Realizar Transferência
            </button>
          </div>
  )
}