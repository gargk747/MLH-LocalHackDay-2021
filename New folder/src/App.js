import Header from "./components/Header";
import Footer from "./components/Footer";
import List from "./components/List";
import { Container } from "react-bootstrap";

function App() {
  return (
    <div className="App">
      <Header />
      <Container>
      <List />
      </Container>
      <Footer />
    </div>
  );
}

export default App;
