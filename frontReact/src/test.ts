/**
 * Calcula el dígito verificador de un RUT chileno.
 * @param rut Cuerpo del RUT sin puntos ni guion (ej: 12345678)
 * @returns El dígito verificador como string ('0'-'9' o 'K')
 */
const calcularDV = (rut: number): string => {
  let suma: number = 0;
  let multiplicador: number = 2;
  
  // Convertimos el número a string y lo recorremos de derecha a izquierda
  const rutString: string = rut.toString();
  
  for (let i = rutString.length - 1; i >= 0; i--) {
    suma += parseInt(rutString[i]) * multiplicador;
    multiplicador = multiplicador === 7 ? 2 : multiplicador + 1;
  }

  const resto: number = 11 - (suma % 11);
  
  if (resto === 11) return "0";
  if (resto === 10) return "K";
  
  return resto.toString();
};

// Ejemplo de uso:
const miRut: number = 12345678;
const dv: string = calcularDV(miRut);

console.log(`El RUT completo es: ${miRut}-${dv}`);