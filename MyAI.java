package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.gamekit.graph.Edge;
import uk.ac.bris.cs.gamekit.graph.Node;
import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.ai.ResourceProvider;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.ui.ai.GraphComponent.Leaf;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.*;
import static uk.ac.bris.cs.scotlandyard.model.Ticket.SECRET;

@ManagedAI("MrX.first")
public class MyAI implements PlayerFactory{
	@Override
	public Player createPlayer(Colour colour) {
		return new MyPlayer();
	}
	public static class MyPlayer implements Player,MoveVisitor {
		ScotlandYardView v;
		List<ScotlandYardPlayer> players;
		ScotlandYardPlayer player;
		Set<Move> mutarivalide1 = new HashSet<>();
		Set<Move> mutarivalide2 = new HashSet<>();
		TicketMove move;
		Move move2;
		int Cround;
		Set<Map<Move, Integer>> MutariBune;

		int PretAbonament(Object e) {
			switch (e.toString()) {
				case "TAXI":
					return -20;
				case "BUS":
					return -40;
				case "UNDERGROUND":
					return -60;
			}
			return 1000;
		}

		//calculate the cost of going from one node to the other
		private int CostDeplasare(ScotlandYardPlayer player, TicketMove ticket) {
			performTicketMove(player, ticket);
			return PretAbonament(ticket.ticket());
		}

		//**                   		**//
		//**                        **//
		//**                        **//
		@Override
		public void makeMove(ScotlandYardView view, int location, Set<Move> moves, Consumer<Move> callback) {
			callback.accept(mrX1(player));
		}

		//**                   		**//
		//**                        **//
		//**                        **//
		//implementation of MoveVisitor
		@Override
		public void visit(PassMove move) {
			performPassMove(findPlayer(move.colour()), move);
		}

		@Override
		public void visit(DoubleMove move) {
			performDoubleMove(findPlayer(move.colour()), move);
		}

		@Override
		public void visit(TicketMove move) {
			performTicketMove(findPlayer(move.colour()), move);
		}

		//**          **//
		//Move  ticket //
		// **        **//
		private void performPassMove(ScotlandYardPlayer player, PassMove move) {
			if (player.isMrX()) {
				Cround++;
			}
		}

		private void performDoubleMove(ScotlandYardPlayer player, DoubleMove move) {
			player.removeTicket(DOUBLE);
			performTicketMove(player, move.firstMove());
			performTicketMove(player, move.secondMove());
			System.out.println("It is working");
		}

		private void performTicketMove(ScotlandYardPlayer player, TicketMove move) {
			//Remove ticket and change location
			player.removeTicket(move.ticket());
			player.location(move.destination());
			System.out.println("It is working");
		}

		private ScotlandYardPlayer findPlayer(Colour c) {
			for (ScotlandYardPlayer player : players) {
				if (player.colour().equals(c)) {
					return player;
				}
			}
			System.out.println("It is working");
			throw new RuntimeException("");
		}

		//**  Valid Moves functions  **//
		private Set<Move> ValidMoveGenerator(ScotlandYardPlayer player) {
			Collection<Edge<Integer, Transport>> edges;
			edges = v.getGraph().getEdgesFrom(v.getGraph().getNode(player.location()));
			Iterator<Edge<Integer, Transport>> iterator = edges.iterator();
			Edge<Integer, Transport> edge;
			boolean collision = false;
			while (iterator.hasNext()) {
				edge = iterator.next();
				if (player.hasTickets(fromTransport(edge.data()))) {
					//Iterate through all detectives and check if destination collide with detective position. If so, CONTINUE;
					for (ScotlandYardPlayer p : players) {
						if ((p.isDetective()) && (p.location() == edge.destination().value())) {
							collision = true;
						}
					}
					if (collision) {
						collision = false;
						continue;
					}
					if (edge.data().equals(Transport.TAXI) || edge.data().equals(Transport.BUS) || edge.data().equals(Transport.UNDERGROUND)) {
						mutarivalide2.add(new TicketMove(player.colour(), fromTransport(edge.data()), edge.destination().value()));
					}
				}
				if (player.hasTickets(SECRET)) {
					mutarivalide2.add(new TicketMove(player.colour(), SECRET, edge.destination().value()));
				}
			}
			//Add double ticket move to move set, disable double ticket on final round
			if (player.hasTickets(DOUBLE) && v.getCurrentRound() < v.getRounds().size() - 1) {
				//Create a temporary set variable to store double turns: add temporary set to valid move set at the end
				Set<Move> temp = new HashSet<>();
				for (Move m : mutarivalide2) {
					edges = v.getGraph().getEdgesFrom(v.getGraph().getNode(((TicketMove) m).destination()));
					iterator = edges.iterator();
					while (iterator.hasNext()) {
						edge = iterator.next();
						if (((TicketMove) m).ticket() == SECRET) {
							if (player.hasTickets(fromTransport(edge.data()))) {
								for (ScotlandYardPlayer p : players) {
									if ((p.isDetective()) && (p.location() == edge.destination().value())) {
										collision = true;
									}
								}
								if (collision) {
									collision = false;
									continue;
								}
								if (edge.data().equals(Transport.TAXI) || edge.data().equals(Transport.BUS) || edge.data().equals(Transport.UNDERGROUND)) {
									temp.add(new DoubleMove(v.getCurrentPlayer(), (TicketMove) m, new TicketMove(player.colour(), fromTransport(edge.data()), edge.destination().value())));
								}
								if (player.hasTickets(SECRET, 2)) {
									temp.add(new DoubleMove(v.getCurrentPlayer(), (TicketMove) m, new TicketMove(player.colour(), SECRET, edge.destination().value())));
								}
							}
						} else if (((TicketMove) m).ticket() == fromTransport(edge.data())) {
							for (ScotlandYardPlayer p : players) {
								if ((p.isDetective()) && (p.location() == edge.destination().value())) {
									collision = true;
								}
							}
							if (collision) {
								collision = false;
								continue;
							}
							if (player.hasTickets(fromTransport(edge.data()), 2)) {
								if (edge.data().equals(Transport.TAXI) || edge.data().equals(Transport.BUS) || edge.data().equals(Transport.UNDERGROUND)) {
									temp.add(new DoubleMove(v.getCurrentPlayer(), (TicketMove) m, new TicketMove(player.colour(), fromTransport(edge.data()), edge.destination().value())));
								}
							}
							if (player.hasTickets(SECRET)) {
								temp.add(new DoubleMove(v.getCurrentPlayer(), (TicketMove) m, new TicketMove(player.colour(), SECRET, edge.destination().value())));
							}
						} else {
							if (player.hasTickets(fromTransport(edge.data()))) {
								for (ScotlandYardPlayer p : players) {
									if ((p.isDetective()) && (p.location() == edge.destination().value())) {
										collision = true;
									}
								}
								if (collision) {
									collision = false;
									continue;
								}
								if (edge.data().equals(Transport.TAXI) || edge.data().equals(Transport.BUS) || edge.data().equals(Transport.UNDERGROUND)) {
									temp.add(new DoubleMove(v.getCurrentPlayer(), (TicketMove) m, new TicketMove(player.colour(), fromTransport(edge.data()), edge.destination().value())));
								}
								if (player.hasTickets(SECRET)) {
									temp.add(new DoubleMove(v.getCurrentPlayer(), (TicketMove) m, new TicketMove(player.colour(), SECRET, edge.destination().value())));
								}
							}
						}
					}
				}
				mutarivalide2.addAll(temp);
			}

			//If valid move set empty, add PassMove
			if (mutarivalide1.isEmpty()) {
				mutarivalide1.add(new PassMove(v.getCurrentPlayer()));
			}
			System.out.println("It is working");
			return mutarivalide1;
		}

		private int NumberOfMoves(ScotlandYardPlayer player) {
			return ValidMoveGenerator(player).size();
		}

		//very bad can execute only ticket move and check when using only Ticket move
		private Move mrX1(ScotlandYardPlayer player) {
			player = findPlayer(Colour.BLACK);
			mutarivalide1 = ValidMoveGenerator(player);
			for (Move m : mutarivalide1) {
				performTicketMove(player, move);
				mutarivalide1 = ValidMoveGenerator(player);
				if (mutarivalide1.size() >= 4) {
					return move;
				}
			}
			throw new RuntimeException("Should not happen");
		}

		// now use move visitor check validity  still very weak
		private Move mrX2(ScotlandYardPlayer player) {
			player = findPlayer(Colour.BLACK);
			mutarivalide1 = ValidMoveGenerator(player);
			for (Move m : mutarivalide1) {
				move2.visit(this);
				mutarivalide1 = ValidMoveGenerator(player);
				if (mutarivalide1.size() >= 4) {
					return move;
				}
				break;
			}
			throw new RuntimeException("Should not happen");
		}

		//implement score
		private int MrXscore() {


			return 0;
		}

		// use later for minMax alghoritm in case of using
		private int Dscore() {


			return 0;
		}
	}
// do both score using minimal distance and any other necessary function
// implement min max wiht alpha-betha prunning (use if needed instead) MonteCarlo Tree search
}

