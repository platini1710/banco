// Contador.jsx
import { useState } from 'react';

export default function Contador() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <h1>Prueba de clics</h1>
      <button onClick={() => setCount(count + 1)}>
        Tienes {count} clics
      </button>
    </div>
  );
}